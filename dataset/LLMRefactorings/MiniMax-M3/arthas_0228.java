public class arthas_0228 {

        @Override
        public void usage(StringBuilder builder, String prefix, CLI cli) {
    
            TableElement table = new TableElement(1, 2).leftCellPadding(1).rightCellPadding(1);
    
            table.add(row().add(label("USAGE:").style(getHighlightedStyle())));
            table.add(row().add(label(computeUsageLine(prefix, cli))));
            table.add(row().add(""));
            table.add(row().add(label("SUMMARY:").style(getHighlightedStyle())));
            table.add(row().add(label("  " + cli.getSummary())));
    
            if (cli.getDescription() != null) {
                String[] descLines = cli.getDescription().split("\\n");
                for (String line: descLines) {
                    if (shouldBeHighlighted(line)) {
                        table.add(row().add(label(line).style(getHighlightedStyle())));
                    } else {
                        table.add(row().add(label(line)));
                    }
                }
            }
    
            if (!cli.getOptions().isEmpty() || !cli.getArguments().isEmpty()) {
                table.add(row().add(""));
                table.row(label("OPTIONS:").style(getHighlightedStyle()));
                for (Option option : cli.getOptions()) {
                    String optionString = buildOptionString(option);
                    table.add(row().add(label(optionString).style(getHighlightedStyle()))
                                    .add(option.getDescription()));
                }
    
                for (Argument argument: cli.getArguments()) {
                    table.add(row().add(label("<" + argument.getArgName() + ">").style(getHighlightedStyle()))
                            .add(argument.getDescription()));
                }
            }
    
            builder.append(RenderUtil.render(table, getWidth()));
        }

        private String buildOptionString(Option option) {
            StringBuilder optionSb = new StringBuilder(32);
    
            // short name
            if (isNullOrEmpty(option.getShortName())) {
                optionSb.append("   ");
            } else {
                optionSb.append('-').append(option.getShortName());
                if (isNullOrEmpty(option.getLongName())) {
                    optionSb.append(' ');
                } else {
                    optionSb.append(',');
                }
            }
            // long name
            if (!isNullOrEmpty(option.getLongName())) {
                optionSb.append(" --").append(option.getLongName());
            }
    
            if (option.acceptValue()) {
                optionSb.append(" <value>");
            }
    
            return optionSb.toString();
        }
}
